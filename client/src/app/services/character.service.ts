import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { firstValueFrom } from 'rxjs';

const CHARS_API = "https://mcudatabase-production.up.railway.app/api/characters"
const CHAR_API = "https://mcudatabase-production.up.railway.app/api/character"

@Injectable({
  providedIn: 'root'
})
export class CharacterService {

  constructor(private http: HttpClient) {}

  getCharacters(search:string, limit: number, offset: number) {

    const params = new HttpParams()
    .append("search", search)
    .append("limit", limit)
    .append("offset", offset)

    return firstValueFrom(this.http.get(CHARS_API, { params }))

  }

  getCharacterById(charId : number) {
    return firstValueFrom(this.http.get(`${CHAR_API}/${charId}`))
  }
}
