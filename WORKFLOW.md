# Git Workflow Guide

This document outlines the development workflow for the project. The `dev` branch is used for development, while the `main` branch is reserved for production-ready code.

---

## Branch Structure
- **main**: The production branch containing stable, production-ready code.
- **dev**: The development branch where new features and fixes are integrated.
- **feature/***: Branches created for specific features or fixes (e.g., `feature/add-login` or `feature/implement-dashboard`).

---

## Workflow Overview
1. All new work is done in feature branches created from `dev`.
2. Developers create pull requests (PRs) to merge feature branches into `dev`.
3. Once the `dev` branch is stable and ready for release, it is merged into `main`.

---

## Steps for Making a Pull Request

### 1. Create a Feature Branch
1. Switch to the `dev` branch:
   ```bash
   git checkout dev
   git pull origin dev

2. Create a new branch for your feature:
    ```bash
   git checkout -b feature/your-feature-name
    ```
   Replace `your-feature-name` with a descriptive name for your feature. Examples:
    - `feature/add-login`
    - `feature/implement-payment-api`
    - `fix/resolve-navbar-issue`
    - `chore/update-readme`

3. Work on your feature, then stage and commit your changes:
    ```bash
    git add .
    git commit -m " feature: commit-message"
    ```
   Replace `Add feature description` with a brief description of the changes you made. Examples:
    - "Fix navbar alignment issue in mobile view"
    - "Add payment API integration for checkout"
    - "Update README with project setup instructions"
4. Push your branch to GitHub:
    ```bash
    git push origin feature/your-feature-name

### 2. Create a Pull Request
1. Go to the repository on GitHub.
2. Click on the Pull requests tab and then click New pull request.
3. Select feature/your-feature-name as the source branch and dev as the target branch.
4. Please follow pull request templates at [Pull Request Templates](https://axolo.co/blog/p/part-3-github-pull-request-template)
5. Assign reviewers and click Create pull request.
---

## Reviewing Code
### 1. Reviewers will be notified of the pull request.
### 2. Reviewers:
- Open the PR on GitHub.
- Use the "Files changed" tab to view and comment on changes.
- Approve or request changes.
### 3. If changes are requested:
- Make the updates on the same branch and push them to GitHub.
- GitHub will automatically update the PR.

---
## Merging a Pull Request
### 1. Merging into `dev`
1. Once the pull request is approved, click Merge pull request.
2. Choose a merge strategy:
    - Squash and merge: Combines all commits into one for a clean history.
    - Rebase and merge: Preserves individual commits but rebases them onto dev.
3. Delete the feature branch after merging *(recommended)*.

### 2. Merging into main
1. When dev is stable and ready for release, create a pull request from dev to main.
2. Follow the same review and merge process as above.
3. Optionally, create a tag for the release
    ```bash
   git checkout main
    git pull origin main
    git tag -a vX.Y.Z -m "Release version X.Y.Z"
    git push origin vX.Y.Z

