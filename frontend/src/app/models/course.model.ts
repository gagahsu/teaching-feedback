export interface Course {
  id: number;
  date: string;
  title: string;
  content: string;
}

export interface CalendarDay {
  titles: string[];
  msgCount: number;
  helpCount: number;
}
