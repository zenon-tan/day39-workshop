import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { SearchComponent } from './components/search/search.component';
import { ListComponent } from './components/list/list.component';
import { CharacterComponent } from './components/character/character.component';
import { CommentComponent } from './components/comment/comment.component';

const routes: Routes = [
  {path: "", component: SearchComponent},
  {path: "characters", component: ListComponent},
  {path: "character/:charId", component: CharacterComponent},
  {path: "comment/:charId", component: CommentComponent},
  {path: "**", redirectTo: "", pathMatch: "full"}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
