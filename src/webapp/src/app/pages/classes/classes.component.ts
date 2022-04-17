import { DatePipe } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { NgxSpinnerService } from 'ngx-spinner';
import { concat, EMPTY, timer } from "rxjs";
import { switchMapTo } from "rxjs/operators";
import { ClassService } from 'src/app/core/services';
import { ActionEnum } from 'src/app/shared/components/cm-table-container/models/config-column.model';
import { DataValue } from 'src/app/shared/components/cm-table-container/models/data-value.model';
import { RemovePopupComponent } from 'src/app/shared/components/comfirmation-popup/remove/remove-popup.component';
import { ClassModalComponent } from './class-modal/class-modal.component';

@Component({
  templateUrl: './classes.component.html',
  styleUrls: ['./classes.component.scss'],
  providers: [DatePipe]
})
export class ClassesComponent implements OnInit {

  public form: FormGroup;

  public classes: any[];
  public cols: any[];

  constructor(private fb: FormBuilder,
              private modalService: NgbModal,
              private spinner: NgxSpinnerService,
              private classService: ClassService) {
  }

  ngOnInit() {
    this.cols = [
      {
        header: "Classe",
        field: "classId"
      },
      {
        header: "Description",
        field: "label"
      },
      {
        header: "Nbr d'étudiants",
        field: "nbrStudents"
      },
      {
        header: "E-mail",
        field: "email"
      },
      {
        header: "Spécialié",
        field: "speciality",
        subfield: "label"
      },
      {
        header: "Catégorie",
        field: "category"
      }
    ];
    this.spinner.show();
    this.initForm();
    this.classService.getClasses().subscribe(classes => {
      this.classes = classes;
      this.spinner.hide();
    });
  }

  public openModal(event?: any) {
    const modal: NgbModalRef = this.modalService.open(ClassModalComponent,
      {
        size: 'xl',
        ariaLabelledBy: 'modal-basic-title',
        keyboard: false,
        backdrop: 'static',
        centered: true
      });
    if (event) {
      modal.componentInstance.update = true;
      modal.componentInstance.data = event;
    } else {
      modal.componentInstance.update = false;
    }
    modal.componentInstance.triggerSave.subscribe((dataValue: DataValue) => {
      this.spinner.show();
      if (dataValue.action === ActionEnum.CREATE) {
        concat(
          this.classService.addClass(dataValue.value).pipe(switchMapTo(EMPTY)),
          timer(1000).pipe(switchMapTo(EMPTY)),
          this.classService.getClasses()
        ).subscribe((classes: any[]) => {
          this.spinner.hide();
          this.classes = classes;
          modal.componentInstance.setIsSaved({ isSaved: true });
        }, error => {
          this.spinner.hide();
          if (error.error.code === 701) {
            modal.componentInstance.setIsSaved({ isSaved: false, code: error.error.code });
          }
        })
      } else if (dataValue.action === ActionEnum.UPDATE) {
        concat(
          this.classService.updateClass(dataValue.value).pipe(switchMapTo(EMPTY)),
          timer(1000).pipe(switchMapTo(EMPTY)),
          this.classService.getClasses()
        ).subscribe((classes: any[]) => {
          this.spinner.hide();
          this.classes = classes;
          modal.componentInstance.setIsSaved({ isUpdated: true });
        }, error => {
          this.spinner.hide();
          if (error.error.code === 701) {
            modal.componentInstance.setIsSaved({ isSaved: false, code: error.error.code });
          }
        })
      }
    });
  }

  public deleteClass(event): void {
    const modal: NgbModalRef = this.initPopUp(RemovePopupComponent);
    modal.componentInstance.config = { title: "Confirmation de suppression", message: "Est-ce que vous confirmez la suppression définitive ?" };
    modal.componentInstance.sendData.subscribe((data: boolean) => {
      if (data) {
        this.spinner.show();
        concat(
          this.classService.deleteClass(event.classId).pipe(switchMapTo(EMPTY)),
          timer(1000).pipe(switchMapTo(EMPTY)),
          this.classService.getClasses()
        ).subscribe((classes: any[]) => {
          this.spinner.hide();
          this.modalService.dismissAll();
          this.classes = classes;
        })
      }
    });
  }

  private initPopUp(content: any): NgbModalRef {
    return this.modalService.open(content,
      {
        size: 'lg',
        ariaLabelledBy: 'modal-basic-title',
        keyboard: false,
        backdrop: 'static',
        centered: true
      });
  }

  public onSearch() {
    if (this.form.valid) {
      this.spinner.show();
      this.classService.searchClass(this.form.value).subscribe(classes => {
        this.classes = classes;
        this.spinner.hide();
      });
    }
  }

  public reset() {
    this.form.reset();
    this.classService.getClasses().subscribe(classes => {
      this.classes = classes;
      this.spinner.hide();
    });
  }

  private initForm(): void {
    this.spinner.show();
    this.form = this.fb.group({
      classId: [null],
      email: [null],
      nbrStudents: [null],
      speciality: [null]
    })
  }

}
