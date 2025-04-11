import service from '@/js/utils/request.js'

export function getCategoryInfo(id) {
    return service.get('/no_auth/category/get_info', {
        params: {
            id: id
        }
    })
}