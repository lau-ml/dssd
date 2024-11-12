FROM node:22.0.0-alpine


WORKDIR /app
COPY package.json ./
COPY package-lock.json ./
RUN npm install -g @angular/cli
RUN npm install
COPY . .
EXPOSE 4200 9229
CMD ["ng", "serve", "--host", "0.0.0.0", "--poll=2000"]
