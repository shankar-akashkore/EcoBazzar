import { Routes } from '@angular/router';
import { Home } from './pages/home/home';
import { Login } from './pages/login/login';
import { Register } from './pages/register/register';
import { Products } from './pages/products/products';
import { Cart } from './pages/cart/cart';
import { Admin } from './pages/admin/admin';

export const routes: Routes = [
   {path:'',component: Home},
   {path:'login',component: Login},
   {path: 'register',component: Register},
   {path: 'products', component: Products},
   {path: 'cart', component: Cart},
   {path: 'admin', component: Admin}
];
