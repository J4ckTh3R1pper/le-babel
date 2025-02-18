export function asyncTimeout(milliseconds) {
    return new Promise(resolve => {
        setTimeout(resolve, milliseconds);
    });
}