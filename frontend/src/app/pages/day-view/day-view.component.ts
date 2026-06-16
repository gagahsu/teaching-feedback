import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { CourseService } from '../../services/course.service';
import { MessageService } from '../../services/message.service';
import { Course } from '../../models/course.model';
import { Message, MessageType, MSG_TYPES, TYPE_ORDER } from '../../models/message.model';

const WD = ['星期日','星期一','星期二','星期三','星期四','星期五','星期六'];

@Component({ selector: 'app-day-view', templateUrl: './day-view.component.html' })
export class DayViewComponent implements OnInit {
  date = '';
  today = this.fmt(new Date());
  course: Course | null = null;
  messages: Message[] = [];

  isEditing = false;
  editTitle = ''; editContent = '';

  filterType = 'all';
  selectedType: MessageType = 'general';
  draft = '';
  replyingTo: number | null = null;
  replyDraft = '';

  readonly TYPES = MSG_TYPES;
  readonly TYPE_ORDER = TYPE_ORDER;

  constructor(
    private route: ActivatedRoute,
    public router: Router,
    public auth: AuthService,
    private courseService: CourseService,
    private messageService: MessageService
  ) {}

  ngOnInit() {
    this.date = this.route.snapshot.paramMap.get('date') ?? '';
    this.loadCourse();
    this.loadMessages();
  }

  loadCourse() {
    this.courseService.getCourse(this.date).subscribe({
      next: c => this.course = c,
      error: () => this.course = null
    });
  }
  loadMessages() {
    this.messageService.getMessages(this.date).subscribe(msgs => this.messages = msgs);
  }

  // --- course ---
  startEdit() {
    this.editTitle = this.course?.title ?? '';
    this.editContent = this.course?.content ?? '';
    this.isEditing = true;
  }
  cancelEdit() { this.isEditing = false; }
  saveCourse() {
    this.courseService.saveCourse(this.date, this.editTitle, this.editContent).subscribe(c => {
      this.course = c; this.isEditing = false;
    });
  }

  // --- messages ---
  get filteredMessages(): Message[] {
    if (this.filterType === 'all') return this.messages;
    if (this.filterType === 'open_help') return this.messages.filter(m => m.type === 'help' && !m.resolved);
    return this.messages.filter(m => m.type === this.filterType);
  }
  get openHelpCount() { return this.messages.filter(m => m.type === 'help' && !m.resolved).length; }
  get typeCounts(): Partial<Record<MessageType,number>> {
    const c: Partial<Record<MessageType,number>> = {};
    this.messages.forEach(m => c[m.type] = (c[m.type] ?? 0) + 1);
    return c;
  }

  cardStyle(m: Message): string {
    if (m.type === 'help' && !m.resolved) return 'padding:15px 16px;border-radius:14px;background:rgba(251,113,133,0.06);border:1px solid rgba(251,113,133,0.32);border-left:3px solid #fb7185;';
    if (m.type === 'help' && m.resolved)  return 'padding:15px 16px;border-radius:14px;background:#0e1726;border:1px solid #1e2a44;border-left:3px solid #34d399;';
    return 'padding:15px 16px;border-radius:14px;background:#0f1b30;border:1px solid #1e2a44;';
  }
  avatarStyle(role: string, sm = false): string {
    const sz = sm ? 'width:28px;height:28px;border-radius:8px;font-size:12px;' : 'width:38px;height:38px;border-radius:11px;font-size:15px;';
    const col = role === 'TEACHER' ? 'background:linear-gradient(150deg,#38bdf8,#0ea5e9);color:#04263a;' : 'background:#1e2a44;color:#93c5fd;';
    return sz + col + 'flex-shrink:0;display:flex;align-items:center;justify-content:center;font-family:\'Space Grotesk\',sans-serif;font-weight:700;';
  }

  postMessage() {
    const text = this.draft.trim();
    if (!text) return;
    this.messageService.postMessage(this.date, this.selectedType, text).subscribe(m => {
      this.messages = [...this.messages, m];
      this.draft = ''; this.selectedType = 'general';
    });
  }
  toggleResolve(m: Message) {
    this.messageService.toggleResolve(m.id).subscribe(updated => {
      this.messages = this.messages.map(x => x.id === updated.id ? updated : x);
    });
  }
  startReply(id: number) { this.replyingTo = id; this.replyDraft = ''; }
  cancelReply() { this.replyingTo = null; this.replyDraft = ''; }
  submitReply(msgId: number) {
    const text = this.replyDraft.trim();
    if (!text) return;
    this.messageService.postReply(msgId, text).subscribe(rep => {
      this.messages = this.messages.map(m =>
        m.id === msgId ? { ...m, replies: [...m.replies, rep] } : m
      );
      this.replyingTo = null; this.replyDraft = '';
    });
  }

  get dayTitle() {
    const p = this.date.split('-').map(Number);
    return `${p[1]} 月 ${p[2]} 日`;
  }
  get dayWeekday() {
    if (!this.date) return '';
    const p = this.date.split('-').map(Number);
    return WD[new Date(p[0], p[1]-1, p[2]).getDay()];
  }
  get isToday() { return this.date === this.today; }

  private fmt(d: Date) { return `${d.getFullYear()}-${String(d.getMonth()+1).padStart(2,'0')}-${String(d.getDate()).padStart(2,'0')}`; }
}
