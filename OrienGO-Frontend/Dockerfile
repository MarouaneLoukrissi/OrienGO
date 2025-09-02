FROM node:22.16.0

RUN npm install -g @angular/cli
WORKDIR /app
COPY package*.json ./
RUN npm install
COPY . .
EXPOSE 4200
CMD ["npm", "start"]
# CMD ["ng", "serve", "--host", "0.0.0.0", "--disable-host-check"]
