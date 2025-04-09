import service from '@/js/utils/request.js'

export function getCategoryDto(id) {
    return service.get('/no_auth/category/get_info', {
        params: {
            id: id
        }
    })
}