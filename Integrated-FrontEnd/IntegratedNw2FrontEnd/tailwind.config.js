/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./index.html",
    "./src/**/*.{vue,js,ts,jsx,tsx}",
  ],
  theme: {
    extend: {

    },
  },
  plugins: [
    require("daisyui"),
    require('tailwindcss-animated')
  ],  
    daisyui: {
      themes: [
        {
          mytheme: {         

        },
        },
        "dark",
      ],
  },
}

