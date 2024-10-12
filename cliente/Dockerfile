FROM node:21.7.3-alpine

WORKDIR /app
COPY package.json ./
COPY package-lock.json ./
RUN npm install -g @angular/cli
RUN npm install
COPY . .
RUN npm run build
EXPOSE 4200
CMD ["ng", "serve", "--host", "0.0.0.0"]
