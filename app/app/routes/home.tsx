import type { Route } from "./+types/home";
import { EcommerceApp } from "../ecommerce/Ecommerce";

export function meta({}: Route.MetaArgs) {
  return [
    { title: "Sankhya App" },
    { name: "description", content: "Bem vindo ao meu Case Tecnico!" },
  ];
}

export default function Home() {
  return <EcommerceApp />;
}
