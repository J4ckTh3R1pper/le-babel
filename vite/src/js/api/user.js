import service from '@/js/utils/request.js'

export function getUserBriefView(categoryId, userId) {
    return service.get('/category/get_user', {
        params: {
            categoryId: categoryId,
            userId: userId
        }
    })
}

export function getUserCache (userId) {
    return service.get("/no_auth/user/get_info", {
        params: {
            id: userId
        }
    })
}