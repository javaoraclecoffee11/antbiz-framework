import {defineStore} from 'pinia';
import {useRouter} from 'vue-router';
import {ref} from 'vue';
import {useRuntimeConfig} from '#app';
import type {NitroFetchOptions} from "nitropack";


export interface User {
    id: string,
    email: string,
    username: string,
    roles: string[]
}

export const useAuthStore = defineStore('auth', () => {
    const config = useRuntimeConfig();
    const router = useRouter();

    const accessToken = ref<string | null>(null);
    const refreshToken = ref<string | null>(null);
    const isAuthenticated = ref<boolean>(false);

    const userInfo = ref<User | null>(null);

    onMounted(async () => {
        if(accessToken.value && refreshToken.value) {
            await fetchUserProfile();
        }
    })

    async function login(email: string, password: string) {
        try {
            const response = await $fetch<{ data: {accessToken: string, refreshToken: string}}>('/auth/login', {
                method: 'POST',
                baseURL: config.public.apiBaseUrl, // backend API URL
                body: {
                    email,
                    password,
                },
            });

            accessToken.value = response.data.accessToken;
            refreshToken.value = response.data.refreshToken;
            isAuthenticated.value = true;

            await fetchUserProfile();

            // 로그인 후 홈 화면으로 이동
            await router.push({name: 'index'});
        } catch (error) {
            console.error('Login failed:', error);
            throw new Error('로그인 실패: 잘못된 자격 증명');
        }
    }

    async function logout() {
        try {
            await authFetch<string>('/auth/logout', {
                method: 'POST',
                baseURL: config.public.apiBaseUrl,
                body: {
                    accessToken: accessToken.value,
                    refreshToken: refreshToken.value,
                },
            });
            accessToken.value = null;
            refreshToken.value = null;
            isAuthenticated.value = false;

            // 로그아웃 후 로그인 페이지로 이동
            await router.push({name: 'login'});
        } catch (error) {
            console.error('Logout failed:', error);
            throw new Error('로그아웃 실패');
        }
    }

    async function refreshAccessToken() {
        try {
            if (!refreshToken.value) {
                throw new Error('Refresh token is missing. Please log in again.');
            }

            const response = await $fetch<{data: { accessToken: string, refreshToken: string }}>('/auth/revoke', {
                method: 'POST',
                baseURL: config.public.apiBaseUrl, // backend API URL
                body: {
                    refreshToken: refreshToken.value,
                },
            });

            accessToken.value = response.data.accessToken;
            refreshToken.value = response.data.refreshToken;
        } catch (error) {
            console.error('Failed to refresh token:', error);
            await logout(); // refreshToken 갱신 실패 시 로그아웃 처리
        }
    }


    async function fetchUserProfile() {
        try {
            const resonse = await authFetch('/auth/me', {
                method: 'GET',
                baseURL: config.public.apiBaseUrl,
            }) as {data: { id: string, email: string, username: string, roles: string[] }};

            userInfo.value = resonse.data;
            isAuthenticated.value = true;
        } catch (error) {
            console.error('Failed to fetch user profile:', error);
            throw new Error('사용자 정보를 가져오는 데 실패했습니다.');
        }
    }

    async function authFetch<T>(
        urlPath: string,
        options: NitroFetchOptions<any>
    ): Promise<T> {
        if (!accessToken.value) {
            await refreshAccessToken();
            await fetchUserProfile();
        }

        try {
            return await $fetch<T>(urlPath, {
                headers: {
                    ...options.headers,
                    Authorization: `Bearer ${accessToken.value}`,
                },
                ...options,
            });
        } catch (error) {
            console.error(`Request to ${urlPath} failed:`, error);
            throw error;
        }
    }

    return {
        accessToken,
        refreshToken,
        isAuthenticated,
        login,
        fetchUserProfile,
        userInfo,
        logout,
        refreshAccessToken,
        authFetch,
    };
}, {
    persist: {
        pick: ['accessToken', 'refreshToken']
    }
});