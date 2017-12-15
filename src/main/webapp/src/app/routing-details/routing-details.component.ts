import { RemoteDeviceService } from './../services/remote-device.service';
import { MidiRouting } from './../models/midi-routing';
import { Component, OnInit } from '@angular/core';
import { BsModalService } from 'ngx-bootstrap/modal';
import { BsModalRef } from 'ngx-bootstrap';
import { Subject } from 'rxjs/Subject';
import { RemoteDevice } from '../models/remote-device';

@Component({
  selector: 'app-routing-details',
  templateUrl: './routing-details.component.html',
  styleUrls: ['./routing-details.component.scss'],
})
export class RoutingDetailsComponent implements OnInit {

  midiRouting: MidiRouting;
  onClose: Subject<boolean>;
  remoteDevices: RemoteDevice[];

  constructor(
    private bsModalRef: BsModalRef,
    private remoteDeviceService: RemoteDeviceService) { }

  ngOnInit() {
    this.onClose = new Subject();

    this.remoteDeviceService.getRemoteDevices().subscribe((remoteDevices: RemoteDevice[]) =>  {
      this.remoteDevices = remoteDevices;
    });
  }

  public onOk(): void {
    this.onClose.next(true);
    this.bsModalRef.hide();
  }

  public onCancel(): void {
    this.onClose.next(false);
    this.bsModalRef.hide();
  }

}
