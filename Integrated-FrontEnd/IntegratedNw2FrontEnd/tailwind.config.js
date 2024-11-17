/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./index.html",
    "./src/**/*.{vue,js,ts,jsx,tsx}",
  ],
  theme: {
    extend: {
      fontFamily: {
        'rubik': ['Rubik', 'sans-serif'],
        'chivo': ['Chivo', 'sans-serif'],
      },
      keyframes: {
        typing: {
          '0%': { width: '0ch' }, // Start with 0 characters visible
          '100%': { width: '27ch' }, // Adjust based on text length (24 characters here)
        },
        blink: {
          '0%, 100%': { borderColor: 'transparent' },
          '50%': { borderColor: 'black' },
        },
        fadeOutCursor: {
          '0%': { opacity: '1' },
          '100%': { opacity: '0' },
        },
      },
      animation: {
        typing: 'typing 4s steps(27, end) forwards', // Adjust duration and steps for text length
        blink: 'blink 0.8s step-end infinite', // Blinking animation
        // fadeOutCursor: 'fadeOutCursor 0.5s forwards', // Cursor fade out
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
    require('tailwindcss-animated'),
  ],
  daisyui: {
    themes: [
      {
        mytheme: {
          // Define your custom theme here
        },
      },
      "dark",
    ],
  },
};