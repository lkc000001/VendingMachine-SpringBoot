import { Component, Input, Output, EventEmitter } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'app-show-message',
  templateUrl: './show-message.component.html',
  styleUrls: ['./show-message.component.css']
})
export class ShowMessageComponent {

  @Input() public model = '';
  @Input() public title = '';
  @Input() public body = '';

  constructor(
    public activeModal: NgbActiveModal
  ) { }

}
