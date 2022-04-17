import { Component, OnInit, ViewEncapsulation } from '@angular/core';
import { IAMService } from 'src/app/core/services';
import { DatePipe } from "@angular/common";
import { NgxSpinnerService } from 'ngx-spinner';

@Component({
    selector: 'users',
    templateUrl: './users.component.html',
    styleUrls: ['./users.component.scss'],
    encapsulation: ViewEncapsulation.None,
    providers: [DatePipe]
})
export class UsersComponent implements OnInit {

    public users: any[];
    public cols: any[];

    public usersLength: number;

    constructor(private spinner: NgxSpinnerService,
        private iamService: IAMService) {
    }

    ngOnInit() {
        this.cols = [
            { header: "Nom", field: "firstName" },
            { header: "Prénom", field: "lastName" },
            { header: "Email",  field: "email" },
            { header: "Role",  field: "roles" }
        ];
        this.spinner.show();
        this.iamService.getUsers().subscribe(users => {
            this.users = users;
            this.usersLength = users.length;
            this.spinner.hide();
        });
    }

}