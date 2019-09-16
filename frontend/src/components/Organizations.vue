<template>
    <div>
        <label for="name">Name: </label><input type="text" id="name" v-model="filters.name" placeholder="Name"/>
        <div v-for="org in organizationList" class="organization-list">
            <div class="organization-list-item">
                {{ org.name }}
            </div>
        </div>
        <button class="organization-button-add" @click="addNew()">Add new</button>

    </div>
</template>

<script>
    import api from "./api";
    export default {
        name: "Organizations",
        data() {
            return {
                filters: {
                    name: null,
                    inn: null,
                    active: null,
                },
                organizationList: [],
                errorMessage: '',
                showModal: false,
            };
        },
        methods: {
            addNew() {
                this.showModal = true;
            }
        },
        mounted() {
            try {
                api.organizationList(this.filters).then(response => {
                    if (!response.error) {
                        console.log(response);
                        this.organizationList = response.data.data;
                    } else {
                        console.log(response);
                        this.errorMessage = response.error;
                    }
                }).catch(e => {
                    console.log(e);
                    this.errorMessage = e.toString()
                });
            } catch(err) {
                console.log(err);
            }

        }
    }
</script>

<style scoped>

</style>