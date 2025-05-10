import { translate } from 'i18n-jsautotranslate'
import { defineStore } from 'pinia'

const useTranslatorStore = defineStore(
    'translator', {
        state: () => ({
            translate: translate
        })
    }
)
export default useTranslatorStore