import {
  forwardRef,
  type ButtonHTMLAttributes,
  type InputHTMLAttributes,
  type LabelHTMLAttributes,
  type ReactNode,
  type SelectHTMLAttributes,
  type TextareaHTMLAttributes,
} from "react";
import { cn } from "@/shared/lib/cn";

type Variante = "primary" | "secondary" | "outline" | "ghost" | "destructive" | "brand";
type Tamanho = "sm" | "md" | "icon";

const variantes: Record<Variante, string> = {
  primary: "bg-primary text-primary-foreground hover:bg-primary/90 shadow-sm",
  brand: "bg-brand text-primary-foreground hover:brightness-105 shadow-sm",
  secondary: "bg-secondary text-secondary-foreground hover:bg-secondary/70",
  outline: "border border-border bg-transparent hover:bg-accent text-foreground",
  ghost: "bg-transparent hover:bg-accent text-foreground",
  destructive: "bg-destructive text-destructive-foreground hover:bg-destructive/90",
};

const tamanhos: Record<Tamanho, string> = {
  sm: "h-8 px-3 text-xs gap-1.5",
  md: "h-9 px-4 text-sm gap-2",
  icon: "h-9 w-9",
};

export type ButtonProps = ButtonHTMLAttributes<HTMLButtonElement> & {
  variante?: Variante;
  tamanho?: Tamanho;
  carregando?: boolean;
};

export function buttonClasses(
  variante: Variante = "primary",
  tamanho: Tamanho = "md",
  className?: string,
) {
  return cn(
    "inline-flex cursor-pointer items-center justify-center rounded-md font-medium transition-[background-color,color,box-shadow,filter] duration-150",
    "focus-visible:outline-none focus-visible:ring-3 focus-visible:ring-ring/25",
    "disabled:pointer-events-none disabled:opacity-55",
    variantes[variante],
    tamanhos[tamanho],
    className,
  );
}

export const Button = forwardRef<HTMLButtonElement, ButtonProps>(function Button(
  { className, variante = "primary", tamanho = "md", carregando, children, disabled, ...props },
  ref,
) {
  return (
    <button
      ref={ref}
      disabled={disabled || carregando}
      className={buttonClasses(variante, tamanho, className)}
      {...props}
    >
      {carregando ? (
        <span className="size-3.5 animate-spin rounded-full border-2 border-current border-t-transparent" />
      ) : null}
      {children}
    </button>
  );
});

export const Input = forwardRef<HTMLInputElement, InputHTMLAttributes<HTMLInputElement>>(
  function Input({ className, ...props }, ref) {
    return (
      <input
        ref={ref}
        className={cn(
          "h-9 w-full rounded-md border border-input bg-card px-3 text-sm text-foreground",
          "transition-[border-color,box-shadow] duration-150 placeholder:text-muted-foreground/80",
          "hover:border-[color-mix(in_oklab,var(--input)_70%,var(--graphite))]",
          "focus-visible:border-brand focus-visible:ring-3 focus-visible:ring-brand/20 focus-visible:outline-none",
          "disabled:cursor-not-allowed disabled:bg-muted disabled:text-muted-foreground",
          "aria-invalid:border-destructive",
          className,
        )}
        {...props}
      />
    );
  },
);

export const Textarea = forwardRef<HTMLTextAreaElement, TextareaHTMLAttributes<HTMLTextAreaElement>>(
  function Textarea({ className, ...props }, ref) {
    return (
      <textarea
        ref={ref}
        className={cn(
          "w-full rounded-md border border-input bg-card px-3 py-2 text-sm text-foreground",
          "transition-[border-color,box-shadow] duration-150 placeholder:text-muted-foreground/80",
          "hover:border-[color-mix(in_oklab,var(--input)_70%,var(--graphite))]",
          "focus-visible:border-brand focus-visible:ring-3 focus-visible:ring-brand/20 focus-visible:outline-none",
          "disabled:cursor-not-allowed disabled:bg-muted",
          className,
        )}
        {...props}
      />
    );
  },
);

export const Select = forwardRef<HTMLSelectElement, SelectHTMLAttributes<HTMLSelectElement>>(
  function Select({ className, children, ...props }, ref) {
    return (
      <select
        ref={ref}
        className={cn(
          "h-9 w-full appearance-none rounded-md border border-input bg-card px-3 pr-8 text-sm text-foreground",
          "bg-[url('data:image/svg+xml;utf8,<svg xmlns=%22http://www.w3.org/2000/svg%22 viewBox=%220 0 24 24%22 fill=%22none%22 stroke=%22%23888%22 stroke-width=%222%22 stroke-linecap=%22round%22><path d=%22m6 9 6 6 6-6%22/></svg>')] bg-[length:16px] bg-[right_0.5rem_center] bg-no-repeat",
          "focus-visible:border-brand focus-visible:ring-3 focus-visible:ring-brand/20 focus-visible:outline-none",
          "disabled:cursor-not-allowed disabled:bg-muted",
          className,
        )}
        {...props}
      >
        {children}
      </select>
    );
  },
);

export function Label({ className, ...props }: LabelHTMLAttributes<HTMLLabelElement>) {
  return (
    <label
      className={cn("text-sm font-medium leading-none text-foreground", className)}
      {...props}
    />
  );
}

export function Field({
  label,
  hint,
  erro,
  htmlFor,
  children,
  className,
}: {
  label: string;
  hint?: string;
  erro?: string;
  htmlFor?: string;
  children: ReactNode;
  className?: string;
}) {
  return (
    <div className={cn("space-y-1.5", className)}>
      <Label htmlFor={htmlFor}>{label}</Label>
      {children}
      {erro ? (
        <p className="text-xs text-destructive">{erro}</p>
      ) : hint ? (
        <p className="text-xs text-muted-foreground">{hint}</p>
      ) : null}
    </div>
  );
}

export function Card({ className, ...props }: React.HTMLAttributes<HTMLDivElement>) {
  return (
    <div
      className={cn("rounded-lg border border-border bg-card text-card-foreground", className)}
      {...props}
    />
  );
}

export function CardHeader({ className, ...props }: React.HTMLAttributes<HTMLDivElement>) {
  return <div className={cn("flex flex-col gap-1 p-5 pb-0", className)} {...props} />;
}

export function CardTitle({ className, ...props }: React.HTMLAttributes<HTMLHeadingElement>) {
  return (
    <h3
      className={cn("font-display text-base font-semibold tracking-tight", className)}
      {...props}
    />
  );
}

export function CardDescription({ className, ...props }: React.HTMLAttributes<HTMLParagraphElement>) {
  return <p className={cn("text-sm text-muted-foreground", className)} {...props} />;
}

export function CardContent({ className, ...props }: React.HTMLAttributes<HTMLDivElement>) {
  return <div className={cn("p-5", className)} {...props} />;
}

type VarianteBadge = "brand" | "highlight" | "neutral" | "success" | "danger" | "outline";

const variantesBadge: Record<VarianteBadge, string> = {
  brand: "bg-brand-soft text-brand-ink border-brand/25",
  highlight: "bg-highlight-soft text-highlight-ink border-highlight/25",
  neutral: "bg-muted text-muted-foreground border-border",
  success: "bg-success/12 text-success border-success/25",
  danger: "bg-destructive/12 text-destructive border-destructive/25",
  outline: "bg-transparent text-foreground border-border",
};

export function Badge({
  variante = "neutral",
  className,
  ...props
}: React.HTMLAttributes<HTMLSpanElement> & { variante?: VarianteBadge }) {
  return (
    <span
      className={cn(
        "inline-flex items-center gap-1 rounded-full border px-2 py-0.5 text-[11px] font-medium tracking-wide",
        variantesBadge[variante],
        className,
      )}
      {...props}
    />
  );
}

export function Separator({ className }: { className?: string }) {
  return <div className={cn("h-px w-full bg-border", className)} />;
}
