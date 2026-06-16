export type MessageType = 'general' | 'suggestion' | 'help' | 'like' | 'done';

export interface Reply {
  id: number;
  text: string;
  createdAt: string;
  authorName: string;
  authorRole: string;
}

export interface Message {
  id: number;
  type: MessageType;
  text: string;
  resolved: boolean;
  createdAt: string;
  authorName: string;
  authorRole: string;
  replies: Reply[];
}

export const MSG_TYPES: Record<MessageType, { label: string; color: string; bg: string; border: string }> = {
  general:    { label: '留言', color: '#7dd3fc', bg: 'rgba(56,189,248,0.14)',  border: 'rgba(125,211,252,0.55)' },
  suggestion: { label: '建議', color: '#fbbf24', bg: 'rgba(251,191,36,0.14)',  border: 'rgba(251,191,36,0.55)'  },
  help:       { label: '求救', color: '#fb7185', bg: 'rgba(251,113,133,0.16)', border: 'rgba(251,113,133,0.65)' },
  like:       { label: '收到', color: '#34d399', bg: 'rgba(52,211,153,0.14)',  border: 'rgba(52,211,153,0.55)'  },
  done:       { label: '心得', color: '#c4b5fd', bg: 'rgba(196,181,253,0.14)', border: 'rgba(196,181,253,0.55)' }
};
export const TYPE_ORDER: MessageType[] = ['general','suggestion','help','like','done'];
