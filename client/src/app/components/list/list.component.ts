import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { Character } from 'src/app/models/Character';
import { CharacterService } from 'src/app/services/character.service';

@Component({
  selector: 'app-list',
  templateUrl: './list.component.html',
  styleUrls: ['./list.component.css']
})
export class ListComponent implements OnInit, OnDestroy {

  param$!: Subscription
  charId!: number
  search!: string
  characters!: Character[]
  comments!: Comment[]

  limit: number = 5
  offset: number = 0

  constructor(private router : Router, private activatedRoute : ActivatedRoute,
    private charSrc : CharacterService) {}

  ngOnInit(): void {
    this.param$ = this.activatedRoute.queryParams.subscribe(
      (param) => {
        this.search = param['search']
        console.info(this.search)
        this.charSrc.getCharacters(this.search, this.limit, this.offset).then(
          (data:any) => {
            this.characters = data['Characters'] as Character[]
            console.info(this.characters)
          }
        )
      }
    )
  }
  ngOnDestroy(): void {
    this.param$.unsubscribe()
  }

  selectChar(idx : number) {

    const selected = this.characters[idx].charId
    this.router.navigate(['/character', selected])

  }

  isPrevious() {
    if(this.offset == 0) {
      return true
    }

    return false
  }

  isNext() {
    if(this.offset > this.characters.length) {
      return true
    }

    return false;
  }

  nextPage() {
    this.offset += this.limit
    this.ngOnInit()
  }

  previousPage() {
    this.offset -= this.limit
    this.ngOnInit()
  }

  

}
