import request from "@/js/utils/request.js"

export function getPostDetails(id) {
    return request.post(
        "/no_auth/post/thread", {
            id: id
        })
}

export function getPopularPosts(pageNum, pageSize) {
    return request.post(
        "/api/no_auth/post/get_posts",
        {
                categoryId: null,
                orderBy: 'views',
                keyword: null,
                pageNum: pageNum,
                pageSize: pageSize,
                ascending: false,
                visibleOnly: true
        }
    )
}