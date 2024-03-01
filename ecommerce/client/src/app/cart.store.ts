
// TODO Task 2

import { Injectable } from "@angular/core";
import { ComponentStore } from "@ngrx/component-store";
import { LineItem } from "./models";

export interface CartSlice {
  loadedOn: number
  lineItems: LineItem[]
}

const INIT_STORE: CartSlice = {
  loadedOn: 0,
  lineItems: []
}

// Use the following class to implement your store
@Injectable()
export class CartStore extends ComponentStore<CartSlice> {
  constructor() { super(INIT_STORE) }

  // Selectors
  readonly getCart = this.select<LineItem[]>(slice => slice.lineItems);

  // // Updaters
  readonly addToCart = this.updater<LineItem>((slice: CartSlice, newItem: LineItem) => {
    return { ...slice, lineItems: [...slice.lineItems, newItem] }
  })

  readonly emptyCart = this.updater((slice: CartSlice) => {
    return { ...slice, lineItems: [] }
  })
}
