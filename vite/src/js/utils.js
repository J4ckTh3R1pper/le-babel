import { axios as ax } from 'axios'

export function asyncTimeout(milliseconds) {
    return new Promise(resolve => {
        setTimeout(resolve, milliseconds);
    });
}

export const axios =  ax.create({
    baseURL: 'http://127.0.0.1:8080',
    timeout: 5000,
    headers: {
        'content-type': 'application/json',
        'X-Requested-With': 'XMLHttpRequest',
    },
    withCredentials: true
});
