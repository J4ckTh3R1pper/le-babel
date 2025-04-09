import service from '@/js/utils/request.js'

export function getUserCache (userId) {
    return service.get("/no_auth/user/get_minimal", {
        params: {
            id: userId
        }
    })
}

export function getMemberCache (categoryId, userId) {
    return service.get("/no_auth/category/get_member", {
        params: {
            categoryId: categoryId,
            userId: userId
        }
    })
}