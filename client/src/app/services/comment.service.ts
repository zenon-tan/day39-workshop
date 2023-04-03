import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { firstValueFrom } from 'rxjs';
import { CComment } from '../models/Comment';

const COMMENT_API = "https://mcudatabase-production.up.railway.app/api/comment"

@Injectable({
  providedIn: 'root'
})
export class CommentService {

  constructor(private http: HttpClient) { }

  saveComment(comment : CComment, charId : number) {

    const json = JSON.stringify(comment)

    const headers = {'content-type': 'application/json', 'Access-Control-Allow-Origin': '*'}

    return firstValueFrom(this.http.post<string>(`${COMMENT_API}/${charId}`, json, { headers }))

  }
}
