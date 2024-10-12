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
      colors:{
        customYellow: '#FFF100',
        customBlue: '#006BFF',
        customSky: '#08C2FF',
        customMint: '#BCF2F6',
        customNavColor: '#8EACCD',
      }
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

