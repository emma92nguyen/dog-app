export interface DogAppRequestLog {
  id: number;
  sessionId: string;
  host: string;
  clientIp: string;
  requestUrl: string;
  logTime: Date;
}
