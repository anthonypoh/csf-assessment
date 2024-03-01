import { Component, OnDestroy, OnInit, ViewChild, inject } from '@angular/core';
import { Observable, Subscription } from 'rxjs';
import { Router } from '@angular/router';
import { CartStore } from './cart.store';
import { LineItem } from './models';
import { ConfirmCheckoutComponent } from './components/confirm-checkout.component';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent implements OnInit, OnDestroy {

  // NOTE: you are free to modify this component
  private router = inject(Router)
  private cartStore = inject(CartStore)

  itemCount!: number
  cartSubscription!: Subscription
  cart: LineItem[] = []

  ngOnInit(): void {
    this.cartSubscription = this.cartStore.getCart.subscribe(cart => {
      this.itemCount = cart.length
      this.cart = cart
    });
  }
  ngOnDestroy(): void {
    this.cartSubscription.unsubscribe()
  }

  checkout(): void {
    this.router.navigate(['/checkout'])
  }
}
