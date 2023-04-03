import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { Character } from 'src/app/models/Character';
import { CComment } from 'src/app/models/Comment';
import { CharacterService } from 'src/app/services/character.service';

@Component({
  selector: 'app-character',
  templateUrl: './character.component.html',
  styleUrls: ['./character.component.css']
})
export class CharacterComponent implements OnInit, OnDestroy {

  param$!: Subscription
  character!: Character
  charId!: number
  comments: CComment[] = []

  constructor(private activatedRoute : ActivatedRoute, 
    private router : Router, private charSrc : CharacterService) {}

  ngOnInit(): void {
    this.param$ = this.activatedRoute.params.subscribe(
      (param) => {
        this.charId = param['charId']
        console.info(this.charId)

        this.charSrc.getCharacterById(this.charId).then(
          (data:any) => {
            this.character = data as Character
            this.comments = this.character.comments

            console.info(this.character)
          }
        )
      }
    )
  }
  ngOnDestroy(): void {
    this.param$.unsubscribe()
  }

  goComment() {
    this.router.navigate(['/comment', this.charId])
  }

  goBack() {
    this.router.navigate(['/'])
  }

    

}
