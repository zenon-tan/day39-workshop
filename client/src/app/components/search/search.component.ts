import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';

@Component({
  selector: 'app-search',
  templateUrl: './search.component.html',
  styleUrls: ['./search.component.css']
})
export class SearchComponent implements OnInit {

  form!: FormGroup

  constructor(private fb: FormBuilder, private router : Router){}

  ngOnInit(): void {
    this.form = this.fb.group({
      search: this.fb.control<string>('', [Validators.required])
    })
  }

  processForm() {
    const search = this.form.value['search']
    this.router.navigate(['/characters'], {queryParams: {search: search}})
  }

  

}
