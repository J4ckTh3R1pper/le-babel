const usePopularPostStore = defineStore(
    'popular_posts', {
        state: () => ({
            pageNum: 0,
            pageSize: 15,
            contents: []
        }),

    }
)