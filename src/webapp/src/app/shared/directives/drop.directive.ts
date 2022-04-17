import { Directive, EventEmitter, HostListener, Output, Input, HostBinding } from '@angular/core';

@Directive({
    selector: '[appDrop]'
})
export class DropDirective {

    @Input() public allowed_extensions: Array<string> = [];
    @Output() public filesChangeEmiter: EventEmitter<File[]> = new EventEmitter();
    @Output() public filesInvalidEmiter: EventEmitter<File[]> = new EventEmitter();

    @HostBinding('style.border') private background = '2px dashed #e4eaec';

    constructor() { }

    @HostListener('dragover', ['$event']) public onDragOver(evt) {
        evt.preventDefault();
        evt.stopPropagation();
    }

    @HostListener('dragleave', ['$event']) public onDragLeave(evt) {
        evt.preventDefault();
        evt.stopPropagation();
    }

    @HostListener('drop', ['$event']) public onDrop(evt) {
        evt.preventDefault();
        evt.stopPropagation();
        let files = evt.dataTransfer.files;
        let valid_files: Array<File> = [];
        let invalid_files: Array<File> = [];
        if (files.length > 0) {
            files.forEach((file: File) => {
                let ext = file.name.split('.')[file.name.split('.').length - 1];
                if (this.allowed_extensions.lastIndexOf(ext) != -1) {
                    valid_files.push(file);
                } else {
                    invalid_files.push(file);
                }
            });
            this.filesChangeEmiter.emit(valid_files);
            this.filesInvalidEmiter.emit(invalid_files);
        }
    }

}