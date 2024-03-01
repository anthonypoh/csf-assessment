import { Component, OnInit, inject } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Cart, LineItem, Order } from '../models';
import { CartStore } from '../cart.store';
import { Subscription } from 'rxjs';
import { ProductService } from '../product.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-confirm-checkout',
  templateUrl: './confirm-checkout.component.html',
  styleUrl: './confirm-checkout.component.css'
})
export class ConfirmCheckoutComponent implements OnInit {

  // TODO Task 3
  private fb = inject(FormBuilder)
  private cartStore = inject(CartStore)
  private productService = inject(ProductService)
  private router = inject(Router)
  form!: FormGroup

  cartSubscription!: Subscription
  cart: LineItem[] = []

  total: number = 0

  ngOnInit(): void {
    this.createForm()
    this.cartSubscription = this.cartStore.getCart.subscribe(cart => {
      this.cart = cart
      this.calculateTotal(cart)
    });
  }

  createForm() {
    this.form = this.fb.group({
      name: this.fb.control<string>('', [Validators.required]),
      address: this.fb.control<string>('', [Validators.required, Validators.minLength(3)]),
      priority: this.fb.control<boolean>(false),
      comments: this.fb.control<string>('')
    })
  }

  calculateTotal(cart: LineItem[]) {
    cart.forEach(item => {
      this.total += item.price
    });
  }

  checkout() {
    const order = this.form.value as Order
    order.cart = { lineItems: this.cart }
    this.productService.checkout(order)
      .then(v => { alert(`OrderId: ${v['OrderId']}`), this.cartStore.emptyCart(), this.router.navigate(['/']) })
      .catch(error => { alert(`message: ${error.error['message']}`) })
  }
}
