/** @type {import('tailwindcss').Config} */
module.exports = {
    content: [
        "./src/main/resources/templates/**/*.html", // Pasta dos templates Thymeleaf
        "./src/main/java/**/*.java" // Utils para o Tailwind CSS de Java
    ],
    theme: {
        extend: {
            fontFamily: {
                'ubuntu': ['Ubuntu Sans', 'sans-serif'],
            }
        },
    },
    plugins: [],
}