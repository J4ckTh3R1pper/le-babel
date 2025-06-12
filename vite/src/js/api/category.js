import service from '@/js/utils/request.js'

export async function getCategoryInfo(id) {
    return service.get('/api/no_auth/category/get_info', {
        params: {
            id: id
        }
    })
}

export async function getCategoryCache(id) {
    return service.get('/api/no_auth/category/get_cache', {
        params: {
            id: id
        }
    })
}

export async function joinCategory(id) {
    return service.put('/api/auth/category/join_category?id=' + id)
}

export async function createCategory(name, avatar, info) {
    return service.postForm("/api/auth/category/create", {
        name: name,
        avatar: avatar,
        info: info,
    })
}