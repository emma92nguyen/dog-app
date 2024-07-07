export class DogBreedListItem {
  number: number = 0;
  breed: string = '';
  labelText: string = '';

  constructor(number: number, breed: string, labelText: string) {
    this.number = number;
    this.breed = breed;
    this.labelText = labelText;
  }
}
