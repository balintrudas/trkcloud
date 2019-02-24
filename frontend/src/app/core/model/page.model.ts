export class Page {
  page: number;
  size: number;
  searchText: string;

  constructor(page: number, size: number, searchText: string) {
    this.page = page;
    this.size = size;
    this.searchText = searchText;
  }
}
