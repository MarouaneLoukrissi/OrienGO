/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    "./src/**/*.{html,ts}",
  ],
  theme: {
    extend: {
      screens: {
        'md-lg': '1300px',
        'lg-up': '1250px',
        'md-up': '941px',
        'sm-md': '745px',
        'smaller-sm-md':'400px',
        'smaller': '280px',
        'xs': '244px',
        'xxs-xs': '220px',
        'xxs': '180px',
      },
      colors: {
        'brand-orange': '#FF6B35',
        'brand-orange-light': '#FF8A65',
        'brand-orange-dark': '#E64A19',
      }
    },
  },
  plugins: [
    require('tailwind-scrollbar-hide')
  ],

}
