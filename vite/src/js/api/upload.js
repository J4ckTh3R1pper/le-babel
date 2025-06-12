import service from "../utils/request";

export function uploadImage(file) {
    const form = new FormData()
    form.append('image', file)
    return service.post("/api/auth/upload_image", form, {
        headers: {
            'Content-Type': 'multipart/form-data'
        }
    })
}