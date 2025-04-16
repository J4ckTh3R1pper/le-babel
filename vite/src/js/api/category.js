import service from '@/js/utils/request.js'

export async function getCategoryInfo(id) {
    return service.get('/no_auth/category/get_info', {
        params: {
            id: id
        }
    })
}

export async function getCategoryCache(id) {
    return service.get('/no_auth/category/get_cache', {
        params: {
            id: id
        }
    })
}

export async function joinCategory(id) {
    return service.put('/category/join_category?id=' + id)
}