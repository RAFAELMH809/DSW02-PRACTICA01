# Frontend

This project was generated using [Angular CLI](https://github.com/angular/angular-cli) version 21.2.3.

## Development server

To start a local development server, run:

```bash
ng serve
```

Once the server is running, open your browser and navigate to `http://localhost:4200/`. The application will automatically reload whenever you modify any of the source files.

## Docker (Nginx only for frontend)

This frontend can run in its own container with Nginx, separate from the backend compose.

Run from `frontend/`:

```bash
docker compose -f docker-compose.frontend.yml up --build -d
```

Then open:

- `http://localhost:4200`

Notes:

- Frontend routes are served with SPA fallback (`index.html`).
- `/api/*` is proxied by Nginx to `http://host.docker.internal:8081/api/*`.
- Keep the backend running on port `8081`.

## Code scaffolding

Angular CLI includes powerful code scaffolding tools. To generate a new component, run:

```bash
ng generate component component-name
```

For a complete list of available schematics (such as `components`, `directives`, or `pipes`), run:

```bash
ng generate --help
```

## Building

To build the project run:

```bash
ng build
```

This will compile your project and store the build artifacts in the `dist/` directory. By default, the production build optimizes your application for performance and speed.

## Running unit tests

To execute unit tests with the [Vitest](https://vitest.dev/) test runner, use the following command:

```bash
ng test
```

## Running end-to-end tests

For end-to-end (e2e) testing, run:

```bash
npx cypress run
```

This project standardizes Cypress as the required E2E framework for frontend critical flows.

## Additional Resources

For more information on using the Angular CLI, including detailed command references, visit the [Angular CLI Overview and Command Reference](https://angular.dev/tools/cli) page.
