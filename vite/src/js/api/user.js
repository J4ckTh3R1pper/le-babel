import service from '@/js/utils/request.js'

export function getUserBriefView(categoryId, userId) {
    return service.get('/category/get_user', {
        params: {
            categoryId: categoryId,
            userId: userId
        }
    })
}