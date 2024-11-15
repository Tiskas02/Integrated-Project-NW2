/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./index.html",
    "./src/**/*.{vue,js,ts,jsx,tsx}",
  ],
  theme: {
    extend: {
      fontFamily: {
        'rubik':[ 'Rubik', 'sans-serif'],
        'chivo': ['Chivo', 'sans-serif']
      },
    },
    screens: {
      'mobile': '100px',
      'tablet': '640px',
      'laptop': '1024px',
      'desktop': '1280px',
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

