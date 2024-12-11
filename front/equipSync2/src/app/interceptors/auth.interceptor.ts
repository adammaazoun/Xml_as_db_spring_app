import { Injectable } from '@angular/core';
import { HttpInterceptor, HttpRequest, HttpHandler, HttpEvent } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {
  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    const token = localStorage.getItem('token');

    // Clone the request to add the Authorization header
    const clonedRequest = token
      ? req.clone({ headers: req.headers.set('Authorization', Bearer ${token}) })
      : req;

    // Pass the cloned request instead of the original request to the next handler
    return next.handle(clonedRequest);
  }
}