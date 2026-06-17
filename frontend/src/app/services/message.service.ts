import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { Message, MessageType, Reply } from '../models/message.model';

@Injectable({ providedIn: 'root' })
export class MessageService {
  private readonly url = `${environment.apiUrl}/messages`;

  constructor(private http: HttpClient) {}

  getMessages(date: string): Observable<Message[]> {
    return this.http.get<Message[]>(`${this.url}/${date}`);
  }

  postMessage(date: string, type: MessageType, text: string, isPrivate = false, taggedCourseId: number | null = null): Observable<Message> {
    return this.http.post<Message>(`${this.url}/${date}`, { type, text, isPrivate, taggedCourseId });
  }

  postReply(msgId: number, text: string): Observable<Reply> {
    return this.http.post<Reply>(`${this.url}/${msgId}/replies`, { text });
  }

  toggleResolve(msgId: number): Observable<Message> {
    return this.http.patch<Message>(`${this.url}/${msgId}/resolve`, {});
  }
}
