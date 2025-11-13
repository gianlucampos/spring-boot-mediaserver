const root = document.documentElement;
const toggleBtn = document.getElementById("theme-toggle");

// Aplica o tema salvo (se houver)
const savedTheme = localStorage.getItem("theme");
if (savedTheme) {
  root.setAttribute("data-theme", savedTheme);
  toggleBtn.textContent = savedTheme === "dark" ? "🌞" : "🌙";
} else {
  // Detecta o padrão do sistema
  const prefersDark = window.matchMedia("(prefers-color-scheme: dark)").matches;
  root.setAttribute("data-theme", prefersDark ? "dark" : "light");
  toggleBtn.textContent = prefersDark ? "🌞" : "🌙";
}

// Alterna tema manualmente
toggleBtn.addEventListener("click", () => {
  const currentTheme = root.getAttribute("data-theme");
  const newTheme = currentTheme === "dark" ? "light" : "dark";
  root.setAttribute("data-theme", newTheme);
  toggleBtn.textContent = newTheme === "dark" ? "🌞" : "🌙";
  localStorage.setItem("theme", newTheme);
});
