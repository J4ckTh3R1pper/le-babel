import {defineConfig, loadEnv} from 'vite'
import vue from '@vitejs/plugin-vue'
import {jsx} from "vue/jsx-runtime";
import vueJsxPlugin from "@vitejs/plugin-vue-jsx";
import AutoImport from 'unplugin-auto-import/vite'
import { ElementPlusResolver } from "unplugin-vue-components/resolvers";
import Components from 'unplugin-vue-components/vite'
import path from "path";
import process from "node:process";

// https://vitejs.dev/config/
export default defineConfig(({ mode}) => {
  // 安装 @types/node 否则无法解析process
  const env = loadEnv(mode, process.cwd())
  console.log(env)
  const { VITE_BACKEND_PORT,VITE_BACKEND_HOST } = env;

  return {
    esbuild: {
      loader: "jsx"
    },
    server: {
      port: 5173,
      host: true,
      open: true,
      proxy: {
        // https://cn.vitejs.dev/config/#server-proxy
        '/api': {
          target: `http://${VITE_BACKEND_HOST}:${VITE_BACKEND_PORT}`,
          changeOrigin: true,
        },
        '/images': {
          target: `http://${VITE_BACKEND_HOST}:${VITE_BACKEND_PORT}/api`,
          changeOrigin: true,
        }
      }
    },
    resolve: {
      alias: {
        'vue': 'vue/dist/vue.esm-bundler.js',
        '@': path.resolve(__dirname, 'src')
      },
      extensions: ['.js', '.vue', '.json'],
    },
    plugins: [
        vue(), vueJsxPlugin(),
        AutoImport({
          imports: [
              'vue',
              'pinia',
              'vue-router',
          ],

          resolvers: [ElementPlusResolver()]
        }),
        Components({
          resolvers: [ElementPlusResolver()]
        })
    ],
    build: {
      outDir: "dist"
    },
  }
})
