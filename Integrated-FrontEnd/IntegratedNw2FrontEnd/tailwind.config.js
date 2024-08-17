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
      'mobile':'320px',
      'tablet':'425px'
    }
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

