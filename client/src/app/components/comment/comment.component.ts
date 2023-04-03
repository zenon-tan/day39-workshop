import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { Character } from 'src/app/models/Character';
import { CComment } from 'src/app/models/Comment';
import { CharacterService } from 'src/app/services/character.service';
import { CommentService } from 'src/app/services/comment.service';

@Component({
  selector: 'app-comment',
  templateUrl: './comment.component.html',
  styleUrls: ['./comment.component.css']
})
export class CommentComponent implements OnInit, OnDestroy {

  param$!: Subscription
  character!: Character
  charId!: number
  form!: FormGroup
  comment!: CComment

  constructor(private activatedRoute : ActivatedRoute, 
    private router : Router, private charSrc : CharacterService, 
    private fb: FormBuilder, private commentSrc: CommentService) {}

  ngOnInit(): void {
    this.param$ = this.activatedRoute.params.subscribe(
      (param) => {
        this.charId = param['charId']
        console.info(this.charId)

        this.charSrc.getCharacterById(this.charId).then(
          (data:any) => {
            this.character = data as Character

            console.info(this.character)
          }
        )
      }
    )

    this.form = this.fb.group({
      comment: this.fb.control<string>('', [Validators.required])
    })
  }

  addComment() {

    this.comment = {
      commentId: "",
      charId: this.charId,
      comment: this.form.value['comment'],
      timestamp: 0

    }

    console.info(this.comment)

    this.commentSrc.saveComment(this.comment, this.charId).then(
      (data: any) => console.info(data)
    )

    this.router.navigate(['/character', this.charId])

  }

  ngOnDestroy(): void {
    this.param$.unsubscribe()
  }

}
