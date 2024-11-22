import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'padLeft',
})
export class PadLeftPipe implements PipeTransform {

  transform(value: any, length: number = 5): string {
    console.log(value);

    if (value === null || value === undefined) {
      return value;
    }
    const stringValue = value.toString();
    return stringValue.padStart(length, '0');
  }

}
