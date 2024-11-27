import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-breadcrumb',
  templateUrl: './breadcrumb.component.html',
  styleUrl: './breadcrumb.component.css'
})
export class BreadcrumbComponent {
  @Input() links: { label: string, url?: string, icon?: string }[] = [];
  @Input() activeLabel: string = '';
  @Input() icon?: string = '';
}
