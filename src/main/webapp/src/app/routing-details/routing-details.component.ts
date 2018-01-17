import { MidiRouting } from './../models/midi-routing';
import { Component, OnInit } from '@angular/core';
import { BsModalService } from 'ngx-bootstrap/modal';
import { BsModalRef } from 'ngx-bootstrap';
import { Subject } from 'rxjs/Subject';
import { RemoteDevice } from '../models/remote-device';
import { SettingsService } from '../services/settings.service';

@Component({
  selector: 'app-routing-details',
  templateUrl: './routing-details.component.html',
  styleUrls: ['./routing-details.component.scss'],
})
export class RoutingDetailsComponent implements OnInit {

  midiRouting: MidiRouting;
  onClose: Subject<number>;
  remoteDevices: RemoteDevice[];

  constructor(
    private bsModalRef: BsModalRef,
    private settingsService: SettingsService) { }

  ngOnInit() {
    this.onClose = new Subject();

    this.settingsService.getSettings().subscribe((result) =>  {
      this.remoteDevices = result.remoteDeviceList;
    });
  }

  public delete(): void {
    // TODO Show yes-no-dialog
    this.onClose.next(3);
    this.bsModalRef.hide();
  }

  public ok(): void {
    this.onClose.next(1);
    this.bsModalRef.hide();
  }

  public cancel(): void {
    this.onClose.next(2);
    this.bsModalRef.hide();
  }

}
